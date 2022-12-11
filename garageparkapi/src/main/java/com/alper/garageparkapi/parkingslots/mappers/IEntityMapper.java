package com.alper.garageparkapi.parkingslots.mappers;

public interface IEntityMapper<E,D>{
    D toDTO(E e);
    E toEntity(D d);
}
