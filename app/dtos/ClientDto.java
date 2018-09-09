package dtos;

import play.data.validation.Constraints;

public class ClientDto {
    @Constraints.Required
    @Constraints.MinLength(1)
    @Constraints.MaxLength(100)
    public String name;
    @Constraints.Required
    public String redirect_uri;
    public String allowed_origin;
    public boolean trusted;
}
