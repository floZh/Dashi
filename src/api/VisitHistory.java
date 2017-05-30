package api;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;

import db.DBConnection;
import db.MongoDBConnection;
import db.MySQLDBConnection;

/**
 * Servlet implementation class VisitHistory
 */
@WebServlet("/history")
public class VisitHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final DBConnection connection = new MySQLDBConnection();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VisitHistory() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//尝试写的HW3，How would you implement your codes such that VisitHistory can return a list of restaurant ids
		//that the user has visited? 
		/*
		try {
			JSONObject input = RpcParser.parseInput(request);
			if (input.has("user_id")) {
				String userId = (String) input.get("user_id");
				JSONArray array = new JSONArray();
				Set<String> res = connection.getVisitedRestaurants(userId) ;
				List<String> outRes = new ArrayList<>(res); 
				//怎样把他转成json格式输出啊
				RpcParser.writeOutput(response, array);
			} else {
				RpcParser.writeOutput(response, new JSONObject().put("status", "InvalidParameter"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}*/
		try {
			JSONArray array = null;
			if (request.getParameterMap().containsKey("user_id")) { //因为存在url里
				String userId = request.getParameter("user_id");
				Set<String> visited_business_id = connection.getVisitedRestaurants(userId);
				array = new JSONArray();
				for (String id : visited_business_id) {
					array.put(connection.getRestaurantsById(id, true));
				}
				RpcParser.writeOutput(response, array);
			} else {
				RpcParser.writeOutput(response, new JSONObject().put("status", "InvalidParameter"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			JSONObject input = RpcParser.parseInput(request);
			if (input.has("user_id") && input.has("visited")) {
				String userId = (String) input.get("user_id");
				JSONArray array = (JSONArray) input.get("visited");
				List<String> visitedRestaurants = new ArrayList<>();
				for (int i = 0; i < array.length(); i++) {
					String businessId = (String) array.get(i);
					visitedRestaurants.add(businessId);
				}
				connection.setVisitedRestaurants(userId, visitedRestaurants);
				RpcParser.writeOutput(response, new JSONObject().put("status", "OK"));
			} else {
				RpcParser.writeOutput(response, new JSONObject().put("status", "InvalidParameter"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			JSONObject input = RpcParser.parseInput(request);
			if (input.has("user_id") && input.has("visited")) {
				String userId = (String) input.get("user_id");
				JSONArray array = (JSONArray) input.get("visited");
				List<String> visitedRestaurants = new ArrayList<>();
				for (int i = 0; i < array.length(); i++) {
					String businessId = (String) array.get(i);
					visitedRestaurants.add(businessId);
				}
				connection.unsetVisitedRestaurants(userId, visitedRestaurants);
				RpcParser.writeOutput(response, new JSONObject().put("status", "OK"));
			} else {
				RpcParser.writeOutput(response, new JSONObject().put("status", "InvalidParameter"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	



	
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		try {
//			JSONObject input = RpcParser.parseInput(request);  //从包里读出来，生成一个object,之所以用RpcParser是因为这部分代码经常被用
//			if (input.has("user_id") && input.has("visited")) {
//				String userId = (String) input.get("user_id");
//				JSONArray array = (JSONArray) input.get("visited");
//				List<String> visitedRestaurants = new ArrayList<>();
//				for (int i = 0; i < array.length(); i++) {
//					String businessId = (String) array.get(i);
//					visitedRestaurants.add(businessId);
//				}
//				RpcParser.writeOutput(response,
//						new JSONObject().put("status", "OK"));
//			} else {
//				RpcParser.writeOutput(response,
//						new JSONObject().put("status", "InvalidParameter"));
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//	}

}
