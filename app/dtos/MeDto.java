package dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import scala.Tuple2;

import java.util.Map;
import java.util.Objects;

/**
 * Created by Selim Eren Bekçe on 16.08.2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeDto {
    private Long id;
    private String name;
    private String email;
    private Map<String, Tuple2<String, Token>> socialLinks;

    public MeDto() {
    }

    public MeDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, Tuple2<String, Token>> getSocialLinks() {
        return socialLinks;
    }

    public void setSocialLinks(Map<String, Tuple2<String, Token>> socialLinks) {
        this.socialLinks = socialLinks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeDto meDto = (MeDto) o;
        return Objects.equals(id, meDto.id) &&
                Objects.equals(name, meDto.name) &&
                Objects.equals(email, meDto.email) &&
                Objects.equals(socialLinks, meDto.socialLinks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, socialLinks);
    }

    @Override
    public String toString() {
        return "MeDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
