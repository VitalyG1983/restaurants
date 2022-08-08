package com.github.vitaly1983g.restaurants.util.validation;

import com.github.vitaly1983g.restaurants.HasId;
import com.github.vitaly1983g.restaurants.error.IllegalRequestDataException;
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

  /*  public static void assureRestIdConsistent(HasId bean, int restId) {
        if (bean.getClass() == Dish.class) {
            Dish dish = (Dish) bean;
            if (dish.getRestId() != restId) {
                throw new IllegalRequestDataException("Dish must has restId=" + restId);
            }
        } else if (bean.getClass() == Vote.class) {
            Vote vote = (Vote) bean;
            if (vote.getRestId() != restId) {
                throw new IllegalRequestDataException("Vote must has restId=" + restId);
            }
        }
    }*/

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

/*    public static void assureMenuDataConsistent(MenuTo menuTo, LocalDate menuDate, int restId) {
        if (!menuTo.getMenuDate().isEqual(menuDate)) {
            throw new IllegalRequestDataException(menuTo.getClass().getSimpleName() + " must has menuDate=" + menuDate);
        } else if (menuTo.getRestId() != restId) {
            throw new IllegalRequestDataException(menuTo.getClass().getSimpleName() + " must has restaurants Id=" + restId);
        }
    }*/
   /* public static void checkNewMenu(MenuTo menuTo, LocalDate menuDate, int restId, MenuRepository menuRepository) {
       // if (menuRepository.getByDate(menuDate, restId).size() != 0) {
        //    throw new IllegalRequestDataException(menuTo.getClass().getSimpleName() + " menu must be new - absent on date=" + menuDate);
       // }
    //}*/
}