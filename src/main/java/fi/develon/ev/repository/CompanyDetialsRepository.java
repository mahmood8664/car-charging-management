package fi.develon.ev.repository;

import fi.develon.ev.entity.CompanyTree;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author mahmood
 * @since 9/12/21
 */
@Component
@AllArgsConstructor
public class CompanyDetialsRepository {

    private final MongoTemplate mongoTemplate;

    public Optional<CompanyTree> getCompanyFlatTree(String companyId, Long maxDepth) {

        final Criteria idCriteria = new Criteria("_id").is(companyId);
        final MatchOperation matchStage = Aggregation.match(idCriteria);

        GraphLookupOperation graphLookupOperation = GraphLookupOperation.builder()
                .from("company")
                .startWith("$_id")
                .connectFrom("_id")
                .connectTo("parentCompanyId")
                .maxDepth(maxDepth)
                .as("childCompanies");

        Aggregation aggregation = Aggregation.newAggregation(matchStage,graphLookupOperation);

        CompanyTree results = mongoTemplate.aggregate(aggregation, "company", CompanyTree.class).getUniqueMappedResult();
        return Optional.ofNullable(results);
    }

}
