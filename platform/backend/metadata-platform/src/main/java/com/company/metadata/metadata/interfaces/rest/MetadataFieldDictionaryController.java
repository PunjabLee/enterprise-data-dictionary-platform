package com.company.metadata.metadata.interfaces.rest;

import com.company.metadata.metadata.application.command.UpdateFieldDefinitionCommand;
import com.company.metadata.metadata.application.query.FieldDictionaryQuery;
import com.company.metadata.metadata.application.service.FieldDictionaryApplicationService;
import com.company.metadata.metadata.interfaces.rest.dto.AssetFieldResponse;
import com.company.metadata.metadata.interfaces.rest.dto.UpdateFieldDefinitionRequest;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 字段字典 REST 接口，提供字段查询和业务定义维护能力。
 *
 * 作者：Punjab
 */
@RestController
@RequestMapping("/dictionary/fields")
public class MetadataFieldDictionaryController {

    private final FieldDictionaryApplicationService fieldDictionaryApplicationService;

    public MetadataFieldDictionaryController(FieldDictionaryApplicationService fieldDictionaryApplicationService) {
        this.fieldDictionaryApplicationService = fieldDictionaryApplicationService;
    }

    @GetMapping
    public List<AssetFieldResponse> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long assetId,
            @RequestParam(required = false) Boolean sensitive,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(defaultValue = "0") int offset
    ) {
        return fieldDictionaryApplicationService.search(new FieldDictionaryQuery(
                        keyword,
                        assetId,
                        sensitive,
                        status,
                        limit,
                        offset
                ))
                .stream()
                .map(AssetFieldResponse::from)
                .toList();
    }

    @PutMapping("/{id}")
    public AssetFieldResponse updateDefinition(
            @PathVariable Long id,
            @RequestBody UpdateFieldDefinitionRequest request
    ) {
        return AssetFieldResponse.from(fieldDictionaryApplicationService.updateDefinition(
                new UpdateFieldDefinitionCommand(
                        id,
                        request.displayName(),
                        request.businessDefinition(),
                        request.businessRule(),
                        request.keyField(),
                        request.sensitive(),
                        request.classificationLevel(),
                        request.standardCode(),
                        request.termCode(),
                        request.ownerUserId(),
                        request.stewardUserId()
                )
        ));
    }
}
