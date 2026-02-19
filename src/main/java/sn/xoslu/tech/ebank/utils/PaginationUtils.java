package sn.xoslu.tech.ebank.utils;

import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.Map;

public class PaginationUtils {
    public static <T> Map<String, Object> createPaginationResponse(
            Page<T> page,
            Map<String, Object> filters
    ) {
        Map<String, Object> response = new HashMap<>();
        response.put("page", page.getNumber());
        response.put("size", page.getSize());
        response.put("totalElements", page.getTotalElements());
        response.put("totalPages", page.getTotalPages());
        response.put("filters", filters);

        return response;
    }
}
