package com.jumunhasyeo.visualization.presentation;

import com.jumunhasyeo.visualization.application.HubVisualizationService;
import com.jumunhasyeo.visualization.dto.VisualizationData;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "HubRoutVisualization", description = "경로 정보 시각화")
@Controller
@RequestMapping("/visualization")
@RequiredArgsConstructor
public class HubVisualizationController {

    private final HubVisualizationService visualizationService;

    @GetMapping("/hubs")
    public String showHubVisualization(Model model) {
        VisualizationData data = visualizationService.getVisualizationData();
        model.addAttribute("visualizationData", data);
        return "hub-visualization";
    }

    @GetMapping("/hubs/data")
    @ResponseBody
    public VisualizationData getVisualizationData() {
        return visualizationService.getVisualizationData();
    }
}
