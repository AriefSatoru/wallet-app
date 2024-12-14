package com.nutech.walletapp.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.nutech.walletapp.model.dtorequest.RegisterUserRequest;
import com.nutech.walletapp.model.dtorequest.TopUpBalanceRequest;
import com.nutech.walletapp.model.dtorequest.UpdateProfileRequest;
import com.nutech.walletapp.model.dtoresponse.CekSaldoResponse;
import com.nutech.walletapp.model.dtoresponse.ProfileResponse;
import com.nutech.walletapp.model.dtoresponse.WebResponse;
import com.nutech.walletapp.service.BalanceService;
import com.nutech.walletapp.service.UserService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BalanceService balanceService;

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    @Value("${file.base-url}")
    private String baseUrl;

    @PostMapping(
        path = "/auth/registration",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> register(@RequestBody RegisterUserRequest request) {
        userService.register(request);
        return WebResponse.<String>builder()
        .status(0)
        .message("Registrasi berhasil silahkan login")
        .data(null).build();
    }

    @GetMapping(
        path = "/balance",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<CekSaldoResponse> cekSaldo(HttpServletRequest request) {
        CekSaldoResponse cekSaldoResponse = new CekSaldoResponse();
        Claims claims = (Claims) request.getAttribute("claims");
        if (claims != null) {
            String email = claims.getSubject();
            cekSaldoResponse = userService.cekSaldo(email);
            return WebResponse.<CekSaldoResponse>builder()
            .status(0)
            .message("Get Balance Berhasil")
            .data(cekSaldoResponse).build();
        }
        return WebResponse.<CekSaldoResponse>builder()
        .status(108)
        .message("Token tidak tidak valid atau kadaluwarsa")
        .data(null).build();
    }

    @PostMapping(
        path = "/topup",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<CekSaldoResponse> topUpSaldo(HttpServletRequest request, @Valid @RequestBody TopUpBalanceRequest request2) {
        CekSaldoResponse cekSaldoResponse = new CekSaldoResponse();
        Claims claims = (Claims) request.getAttribute("claims");
        if (claims != null) {
            String email = claims.getSubject();
            cekSaldoResponse = balanceService.topUpBalance(email, request2);
            return WebResponse.<CekSaldoResponse>builder()
            .status(0)
            .message("Top Up Balance berhasil")
            .data(cekSaldoResponse).build();
        }
        return WebResponse.<CekSaldoResponse>builder()
        .status(108)
        .message("Token tidak tidak valid atau kadaluwarsa")
        .data(null).build();
    }

    @GetMapping(
        path = "/profile",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ProfileResponse> getProfile(HttpServletRequest request) {
        ProfileResponse profileResponse = new ProfileResponse();
        Claims claims = (Claims) request.getAttribute("claims");
        if (claims != null) {
            String email = claims.getSubject();
            profileResponse = userService.getDataUser(email);
            return WebResponse.<ProfileResponse>builder()
            .status(0)
            .message("Sukses")
            .data(profileResponse).build();
        }
        return WebResponse.<ProfileResponse>builder()
        .status(108)
        .message("Token tidak tidak valid atau kadaluwarsa")
        .data(null).build();
    }

    @PutMapping(
        path = "/profile/update",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ProfileResponse> updateProfile(HttpServletRequest request, @RequestBody UpdateProfileRequest request2) {
        ProfileResponse profileResponse = new ProfileResponse();
        Claims claims = (Claims) request.getAttribute("claims");
        if (claims != null) {
            String email = claims.getSubject();
            profileResponse = userService.updateDataUser(email, request2);
            return WebResponse.<ProfileResponse>builder()
            .status(0)
            .message("Update Pofile berhasil")
            .data(profileResponse).build();
        }
        return WebResponse.<ProfileResponse>builder()
        .status(108)
        .message("Token tidak tidak valid atau kadaluwarsa")
        .data(null).build();
    }

    @PutMapping(
        path = "/profile/image",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ProfileResponse> updateProfileImage(HttpServletRequest request, @RequestPart("file") MultipartFile file) {
        ProfileResponse profileResponse = new ProfileResponse();
        Claims claims = (Claims) request.getAttribute("claims");

        // Validasi file jika kosong
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is required");
        }

        // Validasi jenis file
        String contentType = file.getContentType();
        if (!("image/jpeg".equals(contentType) || "image/png".equals(contentType))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Format Image tidak sesuai");
        }

        try {
            //check folder
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            //simpan file ke folder
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.write(filePath, file.getBytes());

            // Buat URL file untuk save ke db
            String fileUrl = baseUrl + "/uploads/" + fileName;

            if (claims != null) {
                String email = claims.getSubject();
                profileResponse = userService.updateDataImageUser(email, fileUrl);
                return WebResponse.<ProfileResponse>builder()
                .status(0)
                .message("Update Profile Image berhasil")
                .data(profileResponse).build();
            }
            return WebResponse.<ProfileResponse>builder()
            .status(108)
            .message("Token tidak tidak valid atau kadaluwarsa")
            .data(null).build();

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Upload Failed");
        }
    }
}
