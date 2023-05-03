package models.lombok;

import lombok.Data;

@Data
public class GetUsersRequestModel {

    String page, per_page, total, total_pages, data;


}
