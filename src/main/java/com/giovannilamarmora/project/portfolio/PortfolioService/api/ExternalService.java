package com.giovannilamarmora.project.portfolio.PortfolioService.api;

import com.giovannilamarmora.project.portfolio.PortfolioService.api.contentful.ContentfulClient;
import com.giovannilamarmora.project.portfolio.PortfolioService.api.contentful.ContentfulException;
import com.giovannilamarmora.project.portfolio.PortfolioService.api.contentful.ContentfulMapper;
import com.giovannilamarmora.project.portfolio.PortfolioService.api.contentful.model.Contentful;
import com.giovannilamarmora.project.portfolio.PortfolioService.app.CMS.DTO.CMSData;
import io.github.giovannilamarmora.utils.generic.Response;
import io.github.giovannilamarmora.utils.interceptors.LogInterceptor;
import io.github.giovannilamarmora.utils.interceptors.LogTimeTracker;
import io.github.giovannilamarmora.utils.interceptors.Logged;
import io.github.giovannilamarmora.utils.interceptors.correlationID.CorrelationIdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Logged
public class ExternalService {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    @Autowired private ContentfulClient contentfulClient;
    @Autowired private ContentfulMapper contentfulMapper;

    @LogInterceptor(type = LogTimeTracker.ActionType.EXTERNAL)
    public ResponseEntity<List<CMSData>> getAndMapCMSData(String locale) {
        ResponseEntity<Contentful> responseEntity = contentfulClient.getCMSData(locale);

        if (responseEntity.getBody() == null || responseEntity.getStatusCode().isError()){
            LOG.error("An error occurred during getting data of contentful");
            throw new ContentfulException("An error occurred during getting data of contentful");
        }
        Contentful contentful = responseEntity.getBody();
        List<CMSData> cmsData = contentfulMapper.fromContentfulToCMSData(contentful);

        String message = "Data successfully founded!";
        Response response =
                new Response(
                        HttpStatus.OK.value(), message, CorrelationIdUtils.getCorrelationId(), cmsData);
        return ResponseEntity.ok(cmsData);
    }
}
