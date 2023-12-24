package application.dataBase.UserDefinedTypes;

import application.dataBase.QueryResultHandler;
import application.entities.Product;
import application.entities.Request;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class RequestResultHandler implements QueryResultHandler<Request> {
    @Override
    public Request handle(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
<<<<<<< HEAD:src/main/java/application/dataBase/UserDefinedTypes/RequestResultHandler.java

=======
>>>>>>> b4b0ea806d18a3c18d5838ad14cb237278639414:src/main/java/Application/dataBase/UserDefinedTypes/RequestResultHandler.java
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            Request request =new Request();
            request.setDone(resultSet.getBoolean("done"));
            request.setDate(LocalDateTime.parse(( CharSequence ) resultSet.getDate("date"), formatter));
            request.setDescription(resultSet.getString("description"));
            request.setId(resultSet.getInt("id"));
            request.setUserId(resultSet.getString("userId"));
            request.setProductId(resultSet.getInt("productId"));
            return request;
        }
        return null;
    }
}
