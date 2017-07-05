/*
 * Copyright 2017 Banco Bilbao Vizcaya Argentaria, S.A..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bbva.arq.devops.ae.mirrorgate.service;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.FeatureStats;
import com.bbva.arq.devops.ae.mirrorgate.core.dto.IssueDTO;
import com.bbva.arq.devops.ae.mirrorgate.mapper.IssueMapper;
import com.bbva.arq.devops.ae.mirrorgate.model.Feature;
import com.bbva.arq.devops.ae.mirrorgate.repository.FeatureRepository;
import com.bbva.arq.devops.ae.mirrorgate.repository.FeatureRepositoryImpl.ProgramIncrementNamesAggregationResult;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

@Service
public class FeatureServiceImpl implements FeatureService{

    @Autowired
    private FeatureRepository repository;

    @Override
    public List<Feature> getActiveUserStoriesByBoards(List<String> boards) {
        return repository.findActiveUserStoriesByBoards(boards, new Sort(new Order("sStatus")));
    }

    @Override
    public List<Feature> getFeatureRelatedIssues(List<String> featuresKeys){
        return repository.findAllBysParentKeyIn(featuresKeys);
    }

    @Override
    public List<Feature> getProductIncrementFeatures(String name){
        return repository.findAllBysPiNamesIn(name);
    }

    @Override
    public ProgramIncrementNamesAggregationResult getProductIncrementFromFeatures(List<String> boards){
        return repository.getProductIncrementFromFeatures(boards);
    }

    @Override
    public List<String> getProgramIncrementFeaturesByBoard(List<String> boards, List<String> programIncrementFeatures){
        return repository.programIncrementBoardFeatures(boards, programIncrementFeatures);
    }

    @Override
    public FeatureStats getFeatureStatsByKeywords(List<String> boards) {
        FeatureStats result = new FeatureStats();

        result.setBacklogEstimate(repository.getBacklogEstimateByKeywords(boards));
        result.setSprintStats(repository.getSprintStatsByKeywords(boards));
        return result;
    }

    @Override
    public Iterable<IssueDTO> saveOrUpdateStories(List<IssueDTO> issues, String collectorId) {

        List<String> ids = issues.stream()
                .map((issue) -> issue.getId().toString())
                .collect(Collectors.toList());

        List<Feature> features = repository.findAllBysIdInAndCollectorId(ids, collectorId);

        Map<String, Feature> entryMap = features.stream()
                .collect(Collectors.toMap(Feature::getsId, (p) -> p));

        features = issues.stream()
                .map((issue) -> {
                    String key = issue.getId().toString();
                    Feature feat = entryMap.containsKey(key) ?
                            entryMap.get(key):
                            new Feature();
                    return IssueMapper.map(issue, feat);
                })
                .collect(Collectors.toList());

        for (Feature feat : features){
            feat.setCollectorId(collectorId);
        }

        return StreamSupport.stream(repository.save(features).spliterator(), false)
                .map((feat) -> new IssueDTO()
                        .setId(Long.parseLong(feat.getsId()))
                        .setName(feat.getsName())
                        .setEstimate(feat.getdEstimate())
                )
                .collect(Collectors.toList());
    }

    @Override
    public void deleteStory(Long id, String collectorId) {
        repository.deleteBysIdAndCollectorId(id.toString(), collectorId);
    }

}
