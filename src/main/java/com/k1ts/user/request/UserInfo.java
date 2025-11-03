package com.k1ts.user.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    private String sub;            // Унікальний ідентифікатор користувача
    private String name;           // Повне ім’я користувача
    private String given_name;     // Ім’я користувача
    private String family_name;    // Прізвище користувача
    private String profile;        // Посилання на профіль користувача
    private String picture;        // URL до аватарки користувача
    private String email;          // Адреса електронної пошти
    private boolean email_verified; // Чи підтверджена електронна пошта
    private String locale;         // Мова користувача
}
