package life.majiang.community.dto;

import lombok.Data;
import life.majiang.community.model.User;

@Data
public class QuestionDTO {
    private Long id;
    private String title;
    private String description;
    private Long gmtCreate;
    private Long gmtModified;
    private Long creator;
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeount;
    private String tag;
    private User user;
}
