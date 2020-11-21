package ru.javawebinar.topjava.util;

import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.model.AbstractBaseEntity;

import java.net.URI;

public final class ControllerUtil {
    private ControllerUtil() {
    }

    public static <T extends AbstractBaseEntity> ResponseEntity<T> createResponseEntity(String path, T entity) {
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(path + "/{id}")
                .buildAndExpand(entity.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(entity);
    }
}
