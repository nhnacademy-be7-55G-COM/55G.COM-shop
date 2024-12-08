package shop.s5g.shop.test.utils;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.snippet.Snippet;

public class RestDocsHelper {
    public static RestDocumentationResultHandler customDocs(String identifier, Snippet... snippets) {
        return document(identifier,
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets
        );
    }

    public static final Snippet SNIPPET_AUTH_HEADER = requestHeaders(headerWithName("Authorization").description("멤버 JWT 액세스 토큰"));
}
