package com.interviewai.resume.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyzeResponse {

    private Integer completeness; // 完整度百分比
    private List<String> keywords; // 关键词列表
    private Integer competitiveness; // 竞争力评分
    private List<String> highlights; // 亮点
    private List<String> risks; // 风险提示
}
