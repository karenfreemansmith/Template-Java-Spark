import org.sql2o.*;
import java.util.List;

public class Item {
  private int id;
  private String item;

  public Item(String description) {
    this.item = description;
    this.save();
  }

  private void save() {
    try(Connection cn = DB.sql2o.open()) {
      String sql = "INSERT INTO items (item) VALUES (:description)";
      this.id = (int) cn.createQuery(sql, true)
        .addParameter("description", this.item)
        .executeUpdate()
        .getKey();
    }
  }

  @Override
  public boolean equals(Object otherItem) {
    if(!(otherItem instanceof Item)) {
      return false;
    } else {
      Item newItem = (Item) otherItem;
      return this.id==newItem.getId() &&
             this.item.equals(newItem.getDescription());
    }
  }

  public static Item find(int id) {
    try(Connection cn = DB.sql2o.open()) {
      String sql = "SELECT * FROM items WHERE id=:id";
      Item item = cn.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Item.class);
      return item;
    }
  }

  public static List<Item> all() {
    String sql = "SELECT * FROM items ORDER BY item";
    try(Connection cn = DB.sql2o.open()) {
      return cn.createQuery(sql).executeAndFetch(Item.class);
    }
  }

  public void delete() {
    try(Connection cn = DB.sql2o.open()) {
      String sql = "DELETE FROM items WHERE id = :id;";
      cn.createQuery(sql)
      .addParameter("id", this.id)
      .executeUpdate();
    }
  }

  public int getId() {
    return this.id;
  }

  public String getDescription() {
    return this.item;
  }

  public void setDescription(String description) {
    this.item = description;
    try(Connection cn = DB.sql2o.open()) {
      String sql = "UPDATE items SET item=:description WHERE id=:id";
      this.id = (int) cn.createQuery(sql, true)
        .addParameter("description", this.item)
        .addParameter("id", this.id)
        .executeUpdate()
        .getKey();
    }
  }

}
