package com.k1ts.password;

import com.k1ts.password.request.change.PasswordChangeRequest;
import com.k1ts.password.request.change.PasswordChangeResponse;
import com.k1ts.password.request.restore.PasswordRestoreResponse;
import com.k1ts.password.request.restore.PasswordRestoreRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PasswordController {
    private final PasswordRestoreService passwordRestoreService;
    private final PasswordChangeService passwordChangeService;

    @PostMapping("/password/restore")
    public PasswordRestoreResponse restore(@RequestBody PasswordRestoreRequest request) {
        return passwordRestoreService.restore(request.getUsername());
    }

    @PostMapping("/password/change")
    public PasswordChangeResponse change(@RequestBody PasswordChangeRequest request) {
        return passwordChangeService.changePassword(request.getToken(), request.getPassword());
    }
}
