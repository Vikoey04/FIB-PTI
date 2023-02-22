package mypackage;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CarRentalNew extends HttpServlet {

  int cont = 0;

  public void doGet(HttpServletRequest req, HttpServletResponse res)
                    throws ServletException, IOException {
    res.setContentType("text/html");
    PrintWriter out = res.getWriter();
    String nombre = req.getParameter("name");

    cont ++;
    
    String model = req.getParameter("co2_rating");
    String engine = req.getParameter("sub_model_vehicle");
    String days = req.getParameter("dies_lloguer");
    String units = req.getParameter("num_vehicles");
    String discount = req.getParameter("descompte");

    boolean error = false;

    if (model.equals("54")) model = "Extralow";
    else if (model.equals("71")) { model = "Low"; }
    else if (model.equals("82")) { model = "Medium"; }
    else if (model.equals("139")) { model = "High"; }
    else { error = true; }

    float disc = Float.parseFloat(discount);
    if (disc > 100) error = true;

    if (!error) {
      out.println("<html><ul><li>Car model: "+model+"</li><li>Engine: "+engine+"</li><li>Days: "+days+"</li><li>Units: "+units+"</li><li>Discount: "+discount+"</li></ul></html>");
    
      File file = new File("my_webapp/rentals.json");
      JSONObject new_rental = new JSONObject();
      new_rental.put("sub_model_vehicle", engine);
      new_rental.put("num_vehicles", units);
      new_rental.put("model_vehicle", model);
      new_rental.put("dies_lloguer", days);
      new_rental.put("descompte", discount);

      JSONObject obj = new JSONObject();
      JSONArray rentals = new JSONArray();

      try (Reader existing_rentals = new FileReader(file)) {
        JSONParser parser = new JSONParser();
        obj = (JSONObject) parser.parse(existing_rentals);
        rentals = (JSONArray) obj.get("rentals");
        if (rentals.size() == 0) rentals = new JSONArray();
        rentals.add(new_rental);

      } catch (Exception e) {
        e.printStackTrace();
      }

      try (FileWriter filewr = new FileWriter("my_webapp/rentals.json")) {
        JSONObject act = new JSONObject();
        act.put("rentals", rentals);
        filewr.write(act.toJSONString());
      } catch (IOException e) {
        e.printStackTrace();
      }

    }
    else {
      out.println("<html><h1>Invalid car rental</h1></html>");
    }

  }

  public void doPost(HttpServletRequest req, HttpServletResponse res)
                    throws ServletException, IOException {
    doGet(req, res);
  }
}
