package sn.xoslu.tech.ebank.mappers;

import sn.xoslu.tech.ebank.dtos.OperationDTO;
import sn.xoslu.tech.ebank.entities.Operation;

import java.util.List;

public interface OperationMapper {
    OperationDTO toDTO(Operation operation);
    Operation toEntity(OperationDTO dto);
    List<OperationDTO> toDTOList(List<Operation> operations);
}
