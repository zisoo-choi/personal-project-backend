package kh.project.demo.domain.mail.controller.form.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessageToUserForm {

    private String to;
}
