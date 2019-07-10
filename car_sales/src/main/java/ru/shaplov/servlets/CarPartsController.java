package ru.shaplov.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.shaplov.logic.ILogic;
import ru.shaplov.logic.LogicDB;
import ru.shaplov.models.Brand;
import ru.shaplov.models.IEntity;
import ru.shaplov.models.ITitledEntity;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author shaplov
 * @since 14.07.2019
 */
@WebServlet("/car_parts")
public class CarPartsController extends HttpServlet {

    private final ILogic logic = LogicDB.getInstance();

    private final Map<String, Function<HttpServletRequest, List<? extends IEntity>>> listsMap = new HashMap<>();

    @Override
    public void init() throws ServletException {
        listsMap.put("brand", request ->
                logic.getList("Brand")
        );
        listsMap.put("model", request -> {
            int brandId = Integer.parseInt(request.getParameter("brandId"));
            Brand brand = (Brand) logic.get(new Brand(brandId));
            return brand.getModels();
        });
        listsMap.put("body", request -> {
            int modelId = Integer.parseInt(request.getParameter("modelId"));
            return logic.getBodyTypes(modelId);
        });
        listsMap.put("engine", request -> {
            int modelId = Integer.parseInt(request.getParameter("modelId"));
            int bodyId = Integer.parseInt(request.getParameter("bodyId"));
            return logic.getEngineTypes(modelId, bodyId);
        });
        listsMap.put("drive", request -> {
            int modelId = Integer.parseInt(request.getParameter("modelId"));
            int bodyId = Integer.parseInt(request.getParameter("bodyId"));
            int engineId = Integer.parseInt(request.getParameter("engineId"));
            return logic.getDriveTypes(modelId, bodyId, engineId);
        });
        listsMap.put("trans", request -> {
            int modelId = Integer.parseInt(request.getParameter("modelId"));
            int bodyId = Integer.parseInt(request.getParameter("bodyId"));
            int engineId = Integer.parseInt(request.getParameter("engineId"));
            int driveId = Integer.parseInt(request.getParameter("driveId"));
            return logic.getTransTypes(modelId, bodyId, engineId, driveId);
        });
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/json");
        String type = req.getParameter("type");
        ObjectMapper mapper = new ObjectMapper();
        List<? extends IEntity> list = listsMap.get(type).apply(req);
        List<MappedObj> res = list.stream()
                .map(v -> new MappedObj(v.getId(), ((ITitledEntity) v).getTitle()))
                .collect(Collectors.toList());
        String json = mapper.writeValueAsString(res);
        PrintWriter out = resp.getWriter();
        out.append(json);
        out.flush();
    }

    /**
     * Class for mapping objects.
     * Contains id and title fields.
     */
    private static class MappedObj {
        private int id;
        private String title;

        private MappedObj(int id, String title) {
            this.id = id;
            this.title = title;
        }

        public MappedObj() {
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
