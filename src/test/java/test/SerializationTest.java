package test;
import dev.quantam.numblejson.NimbleJSON;
import dev.quantam.numblejson.exceptions.JsonDeserializationException;
import dev.quantam.numblejson.exceptions.JsonSerializationException;
import test.model.Person;

public class SerializationTest {
     public static void main(String[] args) throws JsonSerializationException, JsonDeserializationException {
        String s = "Abraham Lincoln";
        int a = 60;
        Person person = new Person(s, a);
         String json = NimbleJSON.serialize(person);
         System.out.println("Serialized JSON:");
         System.out.println(json);

         Person deserializedPerson = NimbleJSON.deserialize(json, Person.class);


         System.out.println("Deserialized Person:");
         System.out.println("Name: " + deserializedPerson.getName());
         System.out.println("Age: " + deserializedPerson.getAge());

    }
}