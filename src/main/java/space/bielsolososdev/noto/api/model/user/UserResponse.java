package space.bielsolososdev.noto.api.model.user;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String email,
        boolean isActive,
        LocalDateTime createdAt,
        Set<String> roles
) {}

