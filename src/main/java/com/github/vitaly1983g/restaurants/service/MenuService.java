package com.github.vitaly1983g.restaurants.service;

import com.github.vitaly1983g.restaurants.model.Menu;
import com.github.vitaly1983g.restaurants.repository.MenuRepository;
import com.github.vitaly1983g.restaurants.to.MenuTo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.github.vitaly1983g.restaurants.util.MenuUtil.createFromTo;

@Service
@AllArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    // private final RestaurantRepository userRepository;

    @Transactional
    public List<Menu> save(MenuTo menuTo, int restId) {
        menuTo.setRestId(restId);
        List<Menu> fromTo = createFromTo(menuTo);

        return menuRepository.saveAll(fromTo);
    }
}
