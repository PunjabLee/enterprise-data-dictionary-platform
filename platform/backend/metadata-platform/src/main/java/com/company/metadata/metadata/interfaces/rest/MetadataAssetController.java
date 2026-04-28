package com.company.metadata.metadata.interfaces.rest;

import com.company.metadata.metadata.application.command.CreateAssetCommand;
import com.company.metadata.metadata.application.command.UpsertAssetFieldCommand;
import com.company.metadata.metadata.application.query.AssetSearchQuery;
import com.company.metadata.metadata.application.service.AssetApplicationService;
import com.company.metadata.metadata.interfaces.rest.dto.AssetFieldRequest;
import com.company.metadata.metadata.interfaces.rest.dto.AssetFieldResponse;
import com.company.metadata.metadata.interfaces.rest.dto.AssetResponse;
import com.company.metadata.metadata.interfaces.rest.dto.AssetVersionResponse;
import com.company.metadata.metadata.interfaces.rest.dto.CreateAssetRequest;
import com.company.metadata.metadata.interfaces.rest.dto.PublishAssetRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 资产目录 REST 接口，提供资产创建、查询、字段维护和发布能力。
 *
 * 作者：Punjab
 */
@RestController
@RequestMapping("/assets")
public class MetadataAssetController {

    private final AssetApplicationService assetApplicationService;

    public MetadataAssetController(AssetApplicationService assetApplicationService) {
        this.assetApplicationService = assetApplicationService;
    }

    @GetMapping
    public List<AssetResponse> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String assetType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String businessDomain,
            @RequestParam(required = false) String systemCode,
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(defaultValue = "0") int offset
    ) {
        return assetApplicationService.search(new AssetSearchQuery(
                        keyword,
                        assetType,
                        status,
                        businessDomain,
                        systemCode,
                        limit,
                        offset
                ))
                .stream()
                .map(AssetResponse::from)
                .toList();
    }

    @PostMapping
    public AssetResponse create(@Valid @RequestBody CreateAssetRequest request) {
        return AssetResponse.from(assetApplicationService.create(new CreateAssetCommand(
                request.assetType(),
                request.code(),
                request.name(),
                request.displayName(),
                request.description(),
                request.businessDomain(),
                request.systemCode(),
                request.ownerUserId(),
                request.stewardUserId(),
                request.attributesJson()
        )));
    }

    @GetMapping("/{id}")
    public AssetResponse findById(@PathVariable Long id) {
        return AssetResponse.from(assetApplicationService.findById(id));
    }

    @GetMapping("/{id}/fields")
    public List<AssetFieldResponse> listFields(@PathVariable Long id) {
        return assetApplicationService.listFields(id).stream()
                .map(AssetFieldResponse::from)
                .toList();
    }

    @PostMapping("/{id}/fields")
    public AssetFieldResponse saveField(
            @PathVariable Long id,
            @Valid @RequestBody AssetFieldRequest request
    ) {
        return AssetFieldResponse.from(assetApplicationService.saveField(new UpsertAssetFieldCommand(
                id,
                request.fieldName(),
                request.displayName(),
                request.dataType(),
                request.lengthValue(),
                request.precisionValue(),
                request.nullable(),
                request.primaryKey(),
                request.keyField(),
                request.sensitive(),
                request.classificationLevel(),
                request.businessDefinition(),
                request.businessRule(),
                request.standardCode(),
                request.termCode(),
                request.ownerUserId(),
                request.stewardUserId()
        )));
    }

    @PostMapping("/{id}/publish")
    public AssetVersionResponse publish(
            @PathVariable Long id,
            @RequestBody(required = false) PublishAssetRequest request
    ) {
        String comment = request == null ? null : request.comment();
        return AssetVersionResponse.from(assetApplicationService.publish(id, comment));
    }

    @GetMapping("/{id}/versions")
    public List<AssetVersionResponse> listVersions(@PathVariable Long id) {
        return assetApplicationService.listVersions(id).stream()
                .map(AssetVersionResponse::from)
                .toList();
    }
}
