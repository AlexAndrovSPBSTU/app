package ru.owen.app.constants;

import io.jsonwebtoken.security.Keys;
import ru.owen.app.dto.CustomerDTO;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public interface ProjectConstants {
    String OWEN_SRC_URL = "https://owen.ru/export/catalog.json?host=owen-energo.ru&key=UHVP4_umq1zYQY689urXDnOOWuuBB83d";
    String KIPPRIBOR_SRC_URL = "https://kippribor.owen.ru/export/catalog.json?host=owen-russia.ru&key=P9WORmwFZuwbtLjeaaGRdfSQR9epBvuW";
    String MEYERTEC_SRC_URL = "https://meyertec.owen.ru/export/catalog.json?host=owen-russia.ru&key=xXeIXcL53NYtEklXxf0VBRDmU4V5ljrb";
    String KIPPRIBOR_PREFIX = "kippribor_";
    String MEYRTEC_PREFIX = "meyrtec_";
    String OWEN_MODIFICATIONS_SRC = "https://owen-russia.ru/wp-content/uploads/wpallimport/files/price_dealer.csv";
    String JWT_KEY = "CU11iT9!dkKnxI6-sKWoOeMzpEoRHbV!sRo=/j0V/KkFVCQa1fZnnT0h7i8K2iwaEOlHU4c9HicA4fny3CQFX0nx3IFPghpCSrW=!8VhsiExIHsQm1teXVNdFdXA4!/0";
    SecretKey KEY = Keys.hmacShaKeyFor(JWT_KEY.getBytes(StandardCharsets.UTF_8));
    int ONE_DAY = 3600_000 * 24;
    String EMAIL = "email";
    String ROLE = "role";
    String USER_ROLE = "ROLE_USER";
    String JWT_HEADER = "Authorization";
    String JWT_ISSUER = "owen";
    String SUBJECT = "JWT Token";
    final Map<String, CustomerDTO> verifiers = new HashMap<>();

    String[][] WHITE_LIST = {
            {
                    "^/verify$",
                    "^/register$",
                    "^/authenticate$",
                    "^/categories$",
                    "^/categories/\\d+$",
                    "^/categories/\\d+/products$",
                    "^/categories/\\d+/products/\\d+$"
            },
            {
                    "/verify",
                    "/register",
                    "/authenticate",
                    "/categories",
                    "/categories/{categoryId}",
                    "/categories/{categoryId}/products",
                    "/categories/{categoryId}/products/{productId}"
            }
    };

    String[] ADMIN_LIST = {
            "/database/clear",
            "/database/reload",

    };
}
