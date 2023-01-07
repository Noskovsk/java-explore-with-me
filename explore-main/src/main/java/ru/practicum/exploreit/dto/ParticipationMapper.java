package ru.practicum.exploreit.dto;

import org.modelmapper.ModelMapper;
import ru.practicum.exploreit.model.ParticipationRequest;

public class ParticipationMapper {
    private static final ModelMapper MODEL_MAPPER = new ModelMapper();

    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
        ParticipationRequestDto participationRequestDto = new ParticipationRequestDto();
        participationRequestDto.setId(participationRequest.getId());
        participationRequestDto.setCreated(participationRequest.getCreated());
        participationRequestDto.setStatus(participationRequest.getStatus());
        participationRequestDto.setEvent(participationRequest.getEvent().getId());
        participationRequestDto.setRequester(participationRequest.getRequester().getId());
        return participationRequestDto;
    }
}
