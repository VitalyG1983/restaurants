package com.github.vitaly1983g.restaurants.util.validation;

import com.github.vitaly1983g.restaurants.HasId;
import com.github.vitaly1983g.restaurants.error.IllegalRequestDataException;
import com.github.vitaly1983g.restaurants.repository.MenuRepository;
import com.github.vitaly1983g.restaurants.to.MenuTo;
import lombok.experimental.UtilityClass;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.lang.NonNull;

import java.time.LocalDate;

@UtilityClass
public class ValidationUtil {

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must be new (id=null)");
        }
    }

    //  Conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
    public static void assureIdConsistent(HasId bean, int id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must has id=" + id);
        }
    }

    public static void checkModification(int count, int id) {
        if (count == 0) {
            throw new IllegalRequestDataException("Entity with id=" + id + " not found");
        }
    }

    //  https://stackoverflow.com/a/65442410/548473
    @NonNull
    public static Throwable getRootCause(@NonNull Throwable t) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        return rootCause != null ? rootCause : t;
    }

    public static void assureMenuDateConsistent(MenuTo menuTo, LocalDate menuDate, int restId, MenuRepository menuRepository) {
        if (menuRepository.getByDate(menuDate, restId).size() == 0) {
            menuTo.setMenuDate(menuDate);
        } else if (menuTo.getMenuDate() != menuDate) {
            throw new IllegalRequestDataException(menuTo.getClass().getSimpleName() + " must has menuDate=" + menuDate);
        }
    }

    public static void checkNewMenu(MenuTo menuTo, LocalDate menuDate, int restId, MenuRepository menuRepository) {
        if (menuRepository.getByDate(menuDate, restId).size() != 0) {
            throw new IllegalRequestDataException(menuTo.getClass().getSimpleName() + " must be new (absent on date menu)");
        }
    }
}