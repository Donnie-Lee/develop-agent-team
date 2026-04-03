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
public class SuggestionsResponse {

    private List<String> contentSuggestions; // 内容优化建议
    private List<String> structureSuggestions; // 结构优化建议
    private List<String> keywordSuggestions; // 关键词优化建议
}
