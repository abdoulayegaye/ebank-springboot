package sn.xoslu.tech.ebank.mappers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import sn.xoslu.tech.ebank.dtos.OperationDTO;
import sn.xoslu.tech.ebank.entities.Operation;
import sn.xoslu.tech.ebank.mappers.AccountMapper;
import sn.xoslu.tech.ebank.mappers.OperationMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OperationMapperImpl implements OperationMapper {

    private final AccountMapper accountMapper;
    @Override
    public OperationDTO toDTO(Operation operation) {
        if (operation == null) return null;
        OperationDTO dto = new OperationDTO();
        dto.setDateOperation(operation.getCreatedAt());
        dto.setAccount(accountMapper.toResponseDTO(operation.getAccount()));
        BeanUtils.copyProperties(operation, dto);
        return dto;
    }

    @Override
    public Operation toEntity(OperationDTO dto) {
        if (dto == null) return null;
        Operation operation = new Operation();
        BeanUtils.copyProperties(dto, operation);
        return operation;
    }

    @Override
    public List<OperationDTO> toDTOList(List<Operation> operations) {
        if (operations == null) return List.of();
        return operations.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
