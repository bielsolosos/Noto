package space.bielsolososdev.noto.api.model.user;

import java.time.LocalDateTime;
import java.util.Set;

public record UserResponse(
        Long id,
        String username,
        String email,
        boolean isActive,
        LocalDateTime createdAt,
        Set<String> roles
) {}

