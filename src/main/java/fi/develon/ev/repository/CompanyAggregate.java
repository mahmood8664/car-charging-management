package fi.develon.ev.repository;

import fi.develon.ev.entity.Company;
import fi.develon.ev.entity.CompanyTree;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GraphLookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author mahmood
 * @since 9/12/21
 */
@Component
@AllArgsConstructor
public class CompanyAggregate {

    private final MongoTemplate mongoTemplate;

    public Optional<Company> getCompanyFlatTree(String companyId, Long maxDepth) {

        final Criteria byNodeId = new Criteria("_id").is(companyId);
        final MatchOperation matchStage = Aggregation.match(byNodeId);

        GraphLookupOperation graphLookupOperation = GraphLookupOperation.builder()
                .from("company")
                .startWith("$_id")
                .connectFrom("_id")
                .connectTo("parentCompanyId")
                .maxDepth(maxDepth)
                .as("childCompanies");

        Aggregation aggregation = Aggregation.newAggregation(matchStage, graphLookupOperation);

        Company results = mongoTemplate.aggregate(aggregation, "company", CompanyTree.class).getUniqueMappedResult();
        return Optional.ofNullable(results);
    }

}
