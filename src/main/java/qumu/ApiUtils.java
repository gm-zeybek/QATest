package qumu;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class ApiUtils {


    public static long countIds(String path){
        Response response = get(path);
        JsonPath jsonPath = new JsonPath(response.asString());
        List<Map<String,Object>> data = jsonPath.getList("data");
        return  data.stream().map( user->user.get("id")).count();
    }

    public static Response create_user_with_name_and_job(String name, String job) {
        JSONObject json = new JSONObject();
        json.put("name",name);
        json.put("job",job);

        return          given()
                .contentType("application/json")
                .body(json)
                .when()
                .post("/users");
    }
}
