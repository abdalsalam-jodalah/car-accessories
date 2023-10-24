package Application.Services;
import Application.entities.Product;
import Application.interfaces_implementators.Premetive_Objects.IntResultHandler;
import Application.interfaces_implementators.Premetive_Objects.ResultSetResultHandler;
import Application.interfaces_implementators.Premetive_Objects.StringResultHandler;
import Application.interfaces_implementators.QueryResultHandler;
import Application.interfaces_implementators.UserDefinedTypes.ProductResultHandler;
import java.lang.reflect.Field;
import java.sql.*;




public class DatabaseService {

  private static Connection connection;
  private static final String databaseNameAndUser = "sql12654012";
  private static final String databasePassword = "wzRUn4Cfmj";


  //construct and establish the connection
  public DatabaseService(){
    try{
      connection = DriverManager.getConnection("jdbc:mysql://sql12.freesqldatabase.com:3306/sql12654012", databaseNameAndUser, databasePassword);
      System.out.println("\nConnected to the database!\n");
    } catch ( SQLException e) {
      e.printStackTrace();
    }

  }

  //close the connection
  public void closeConnection() {
    if (connection != null) {
      try {
        connection.close();
        System.out.println("\nConnection closed successfully.\n");
      } catch ( SQLException e ) {
        e.printStackTrace(); // Handle any potential exceptions here
      }
    }
  }

  //do the query (SELECT with all possible scenarios) and return the result
  public <T> T executeQuery(String query, QueryResultHandler<T> resultHandler){

   try{
     PreparedStatement statement = connection.prepareStatement(query);
     ResultSet resultSet = statement.executeQuery();
     return resultHandler.handle(resultSet);

   }catch ( SQLException e ){
     e.printStackTrace();
     return null;
   }

  }

  //add new object to the database (static -> call it from class name)
  public static <T> void addObject(T object, String tableName){
    try{


      StringBuilder insertQuery = new StringBuilder("INSERT INTO " + tableName + " (");

      //get the fields names (class must have getters)
      Field[] fields = object.getClass().getDeclaredFields();
      for (int i = 0; i < fields.length; i++) {
        insertQuery.append(fields[i].getName());
        if (i < fields.length - 1) {
          insertQuery.append(", ");
        }
      }


      //add ? in the VALUES (?, ?, ?) -> PreparedStatement
      insertQuery.append(") VALUES (");
      for (int i = 0; i < fields.length; i++) {
        insertQuery.append("?");
        if (i < fields.length - 1) {
          insertQuery.append(", ");
        }
      }
      insertQuery.append(")");

      //replace ? with actual values
      PreparedStatement statement = connection.prepareStatement(insertQuery.toString());
      for (int i = 0; i < fields.length; i++) {
        fields[i].setAccessible(true);
        statement.setObject(i + 1, fields[i].get(object));
      }

      //execute updates
      statement.executeUpdate();
      statement.close();

    }catch ( Exception e ){
      e.printStackTrace();
    }
  }

  //delete object based on id (static -> call it from class name)
  public static void deleteObject(int id, String tableName) throws SQLException {

    String deleteQuery = "DELETE FROM " + tableName + " WHERE id = ?";
    PreparedStatement statement = connection.prepareStatement(deleteQuery);
    statement.setInt(1, id);

    statement.executeUpdate();
    statement.close();
  }



  //update object base on id (static -> call it from class name)
  /*(object -> new instance with type of the object you need to update,
    set id to be equal to the id in the record in the database you need to update)
    (tableName -> table name in the database which contains the record you want to update)
    (primaryKeyField -> the primary key in the table contains the record you want to update*/
  public static <T> void updateObject(T object, String tableName, String primaryKeyField){

    try{

      StringBuilder updateQuery = new StringBuilder("UPDATE " + tableName + " SET ");

      //get the fields names (class must have getters)
      Field[] fields = object.getClass().getDeclaredFields();

      for (int i = 0; i < fields.length; i++) {
        if (!fields[i].getName().equals(primaryKeyField)) {
          updateQuery.append(fields[i].getName()).append(" = ?");
          if (i < fields.length - 1) {
            updateQuery.append(", ");
          }
        }
      }

      updateQuery.append(" WHERE ").append(primaryKeyField).append(" = ?");

      PreparedStatement statement = connection.prepareStatement(updateQuery.toString());

      int paramIndex = 1;
      for (Field field : fields) {
        if (!field.getName().equals(primaryKeyField)) {
          field.setAccessible(true);
          statement.setObject(paramIndex, field.get(object));
          paramIndex++;
        }
      }

      Field primaryKey = object.getClass().getDeclaredField(primaryKeyField);
      primaryKey.setAccessible(true);
      statement.setObject(paramIndex, primaryKey.get(object));

      statement.executeUpdate();
      statement.close();

    }catch ( Exception e ){
      e.printStackTrace();
    }



  }
























  public static void main(String[] args) throws SQLException {


    DatabaseService dbs = new DatabaseService();

    //if you need to return int
    int id = dbs.executeQuery("SELECT id FROM Product WHERE name='Spoiler'", new IntResultHandler());
    System.out.println(id);

    //if you need to return string
    String name = dbs.executeQuery("SELECT name FROM Product WHERE id=3", new StringResultHandler());
    System.out.println(name);

    //if you need to return ResultSet
    ResultSet rs = dbs.executeQuery("SELECT * FROM Product WHERE id=1", new ResultSetResultHandler());
    while ( rs.next() )
      System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getDouble(4) + " " + rs.getInt(5));

    //if you need to return user-defined object and put it in new object
    Product product = dbs.executeQuery("SELECT * FROM Product WHERE id=2", new ProductResultHandler());
    System.out.println(product.getId() + " " + product.getName() + " " + product.getPrice() + " " + product.getCategory() + " " + product.getQuantity());


    //add new object
    Product productToBeAdded = new Product(4, "new", "new", 20.0, 20);
    DatabaseService.addObject(productToBeAdded, "Product");

    //delete an object
    DatabaseService.deleteObject(4, "Product");

    //update an object
    Product p = new Product(1, "Spoiler", "exterior", 45.0, 10);
    DatabaseService.updateObject(p, "Product", "id");



    dbs.closeConnection();

  }

}
