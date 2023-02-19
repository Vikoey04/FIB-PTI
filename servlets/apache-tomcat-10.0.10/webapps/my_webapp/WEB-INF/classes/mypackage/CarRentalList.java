package mypackage;

import java.io.*;
import java.util.Iterator;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CarRentalList extends HttpServlet {

  int cont = 0;

  public void doGet(HttpServletRequest req, HttpServletResponse res)
                    throws ServletException, IOException {
    res.setContentType("text/html");
    PrintWriter out = res.getWriter();
    String nombre = req.getParameter("userid");
    cont ++;
    out.println("<html><big>Hola Amigo "+ nombre + "</big><br>"+
                cont + " Accesos desde su carga.</html>");

    try (Reader reader = new FileReader("rentals.json")) {
      JSONParser parser = new JSONParser();
      JSONObject obj = (JSONObject) parser.parse(reader);
      JSONArray rentals = (JSONArray) obj.get("rentals");
      Iterator<JSONObject> iterator = rentals.iterator();
      while (iterator.hasNext()) {
        JSONObject rental = iterator.next();
        String engine = (String) rental.get("sub_model_vehicle");
        String units = (String) rental.get("num_vehicles");
        String model = (String) rental.get("model_vehicle");
        String days = (String) rental.get("dies_lloguer");
        String discount = (String) rental.get("descompte");
        out.println("<html><ul><li>Car model: "+model+"</li><li>Engine: "+engine+"</li><li>Days: "+days+"</li><li>Units: "+units+"</li><li>Discount: "+discount+"</li></ul></html>");
        
      }
      

    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  public void doPost(HttpServletRequest req, HttpServletResponse res)
                    throws ServletException, IOException {
    doGet(req, res);
  }
}
