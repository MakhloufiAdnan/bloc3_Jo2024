package fr.bloc_jo2024.service;

import fr.bloc_jo2024.entity.Offre;
import fr.bloc_jo2024.entity.enums.StatutOffre;
import fr.bloc_jo2024.exception.ResourceNotFoundException;
import fr.bloc_jo2024.repository.OffreRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class OffreService {

    private static final String OFFER_NOT_FOUND = "Offre introuvable avec l'ID : ";

    private final OffreRepository offreRepository;

    public OffreService(OffreRepository offreRepository) {
        this.offreRepository = offreRepository;
    }

    @Transactional
    public Offre creerOffre(Offre offre) {
        if (offre.getQuantite() < 0) {
            throw new IllegalArgumentException("La quantité ne peut pas être négative");
        }
        if (offre.getPrix() < 0) {
            throw new IllegalArgumentException("Le prix ne peut pas être négatif");
        }
        return offreRepository.save(offre);
    }

    @Transactional
    public Offre modifierQuantite(Long idOffre, int nouvelleQuantite) {
        if (nouvelleQuantite < 0) {
            throw new IllegalArgumentException("Quantité invalide");
        }
        Offre offre = offreRepository.findById(idOffre)
                .orElseThrow(() -> new ResourceNotFoundException(OFFER_NOT_FOUND + idOffre));
        offre.setQuantite(nouvelleQuantite);
        return offreRepository.save(offre);
    }

    @Transactional
    public Offre updatePrix(Long idOffre, double nouveauPrix) {
        if (nouveauPrix < 0) {
            throw new IllegalArgumentException("Le prix ne peut pas être négatif");
        }
        Offre offre = offreRepository.findById(idOffre)
                .orElseThrow(() -> new ResourceNotFoundException(OFFER_NOT_FOUND + idOffre));
        offre.setPrix(nouveauPrix);
        return offreRepository.save(offre);
    }

    @Transactional
    public Offre modifierStatutOffre(Long idOffre, StatutOffre nouveauStatut) {
        Offre offre = offreRepository.findById(idOffre)
                .orElseThrow(() -> new ResourceNotFoundException(OFFER_NOT_FOUND + idOffre));

        if (offre.getStatutOffre() == StatutOffre.ANNULE) {
            throw new IllegalStateException("Impossible de modifier une offre annulée");
        }

        offre.setStatutOffre(nouveauStatut);
        return offreRepository.save(offre);
    }
}