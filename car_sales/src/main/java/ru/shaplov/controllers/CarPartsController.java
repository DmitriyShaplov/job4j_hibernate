package ru.shaplov.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.shaplov.logic.ILogicParts;
import ru.shaplov.models.Brand;
import ru.shaplov.models.IEntity;
import ru.shaplov.models.ITitledEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author shaplov
 * @since 14.07.2019
 */
@Controller
public class CarPartsController {

    private final Map<String, Function<Map<String, String>, List<? extends IEntity>>> listsMap = new HashMap<>();

    @Autowired
    public CarPartsController(ILogicParts logicParts) {
        listsMap.put("brand", request ->
                logicParts.findAllBrand()
        );
        listsMap.put("model", params -> {
            int brandId = Integer.parseInt(params.get("brandId"));
            Brand brand = logicParts.getBrandById(brandId);
            return brand.getModels();
        });
        listsMap.put("body", params -> {
            int modelId = Integer.parseInt(params.get("modelId"));
            return logicParts.getBodyTypes(modelId);
        });
        listsMap.put("engine", params -> {
            int modelId = Integer.parseInt(params.get("modelId"));
            int bodyId = Integer.parseInt(params.get("bodyId"));
            return logicParts.getEngineTypes(modelId, bodyId);
        });
        listsMap.put("drive", params -> {
            int modelId = Integer.parseInt(params.get("modelId"));
            int bodyId = Integer.parseInt(params.get("bodyId"));
            int engineId = Integer.parseInt(params.get("engineId"));
            return logicParts.getDriveTypes(modelId, bodyId, engineId);
        });
        listsMap.put("trans", params -> {
            int modelId = Integer.parseInt(params.get("modelId"));
            int bodyId = Integer.parseInt(params.get("bodyId"));
            int engineId = Integer.parseInt(params.get("engineId"));
            int driveId = Integer.parseInt(params.get("driveId"));
            return logicParts.getTransTypes(modelId, bodyId, engineId, driveId);
        });
    }

    @GetMapping(value = "/car_parts", produces = "application/json;charset=UTF-8")
    @ResponseBody
    protected List<MappedObj> getPart(@RequestParam Map<String, String> params) {
        String type = params.get("type");
        List<? extends IEntity> list = listsMap.get(type).apply(params);
        return list.stream()
                .map(v -> new MappedObj(v.getId(), ((ITitledEntity) v).getTitle()))
                .collect(Collectors.toList());
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
