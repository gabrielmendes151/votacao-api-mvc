package com.votacaoapi.model;

import com.votacaoapi.enums.Voto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PAUTAS_VOTADAS_ASSOCIADOS")
public class PautasVotadasAssociados {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(targetEntity = Pauta.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "pauta_id")
    private Pauta pauta;

    @ManyToOne(targetEntity = Associado.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "associado_id")
    private Associado associado;

    @Enumerated(value = EnumType.STRING)
    private Voto voto;
}
