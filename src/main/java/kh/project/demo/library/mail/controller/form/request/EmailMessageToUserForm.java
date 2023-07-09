package kh.project.demo.library.mail.controller.form.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessageToUserForm {

    private String to;
}
