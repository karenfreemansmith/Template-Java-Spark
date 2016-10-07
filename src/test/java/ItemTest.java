import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;

public class ItemTest {
  private Item item1;
  private Item item2;

  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/test", null, null);
    item1 = new Item("item one");
    item2 = new Item("item two");
  }

  @Test
  public void Item_instantiates_true() {
    assertEquals(true, item1 instanceof Item);
  }

  @Test
  public void save_returnsIdFromDatabase_true() {
    assertEquals(true, item1.getId()>0);
  }

  @Test
  public void find_returnCorrectItem_true() {
    assertTrue(Item.find(item1.getId()).equals(item1));
  }

  @Test
  public void all_returnsAllInstances_true() {
    assertTrue(Item.all().size()>1);
  }

  @Test
  public void delete_deletesItem_true() {
    int itemId = item2.getId();
    item2.delete();
    assertEquals(null, Item.find(itemId));
  }

  @Test
  public void getDescription_returnsCorrectName_String() {
    assertEquals("item one", item1.getDescription());
  }

  @Test
  public void setDescription_updatesDescription_String() {
    item2.setDescription("other item");
    assertEquals("other item", Item.find(item2.getId()).getDescription());
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM items *;";
      con.createQuery(sql).executeUpdate();
    }
  }
}
