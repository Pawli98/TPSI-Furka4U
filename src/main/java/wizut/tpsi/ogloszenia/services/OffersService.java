/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wizut.tpsi.ogloszenia.services;

import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import wizut.tpsi.ogloszenia.jpa.BodyStyle;
import wizut.tpsi.ogloszenia.jpa.CarManufacturer;
import wizut.tpsi.ogloszenia.jpa.CarModel;
import wizut.tpsi.ogloszenia.jpa.FuelType;
import wizut.tpsi.ogloszenia.jpa.Offer;
import wizut.tpsi.ogloszenia.web.OfferFilter;

/**
 *
 * @author pawel
 */
@Service
@Transactional
public class OffersService {

    @PersistenceContext
    private EntityManager em;

    public CarManufacturer getCarManufacturer(int id) {
        return em.find(CarManufacturer.class, id);
    }

    public CarModel getCarModel(int id) {
        return em.find(CarModel.class, id);
    }

    public List<CarManufacturer> getCarManufacturers() {
        String jpql = "select cm from CarManufacturer cm order by cm.name";
        TypedQuery<CarManufacturer> query = em.createQuery(jpql, CarManufacturer.class);
        List<CarManufacturer> result = query.getResultList();
        return result;
    }

    public List<BodyStyle> getBodyStyles() {
        String jpql = "select cm from BodyStyle cm order by cm.name";
        TypedQuery<BodyStyle> query = em.createQuery(jpql, BodyStyle.class);
        List<BodyStyle> result = query.getResultList();
        return result;
    }

    public List<FuelType> getFuelTypes() {
        String jpql = "select cm from FuelType cm order by cm.name";
        TypedQuery<FuelType> query = em.createQuery(jpql, FuelType.class);
        List<FuelType> result = query.getResultList();
        return result;
    }

    public List<CarModel> getCarModels() {
        String jpql = "select cm from CarModel cm order by cm.name";
        TypedQuery<CarModel> query = em.createQuery(jpql, CarModel.class);
        List<CarModel> result = query.getResultList();
        return result;
    }

    public List<CarModel> getCarModels(int manufacturerId) {
        String jpql = "select cm from CarModel cm where cm.manufacturer.id = :id order by cm.name";

        TypedQuery<CarModel> query = em.createQuery(jpql, CarModel.class);
        query.setParameter("id", manufacturerId);

        return query.getResultList();
    }

    public Offer getOffer(int offerId) {
        String jpql = "select offer from Offer offer where offer.id = :id";

        TypedQuery<Offer> query = em.createQuery(jpql, Offer.class);
        query.setParameter("id", offerId);

        return query.getSingleResult();
    }

    public List<Offer> getOffers() {
        return em.createQuery("select ofr from Offer ofr order by ofr.price", Offer.class).getResultList();
    }

    public List<Offer> getOffersByModel(int modelId) {
        String jpql = "select cm from Offer cm where cm.model.id = :id order by cm.id";

        TypedQuery<Offer> query = em.createQuery(jpql, Offer.class);
        query.setParameter("id", modelId);

        return query.getResultList();
    }

    public List<Offer> getOffersByManufacturer(int manufacturerId) {
        String jpql = "select cm from Offer cm where cm.model.manufacturer.id = :id order by cm.id";
        TypedQuery<Offer> query = em.createQuery(jpql, Offer.class);
        query.setParameter("id", manufacturerId);

        return query.getResultList();
    }

    public CarModel getModel(int id) {
        return em.find(CarModel.class, id);
    }

    public List<CarModel> getManufacturerModels(int modelId) {
        CarModel found = em.find(CarModel.class, modelId);
        int Id_manufacturer = found.getManufacturer().getId();

        String jpql = "select cm from car_model cm where cm.manufacturer.id = :id order by cm.id";
        TypedQuery<CarModel> query = em.createQuery(jpql, CarModel.class);
        query.setParameter("id", Id_manufacturer);
        return query.getResultList();
    }

    public Offer createOffer(Offer offer) {
        em.persist(offer);
        return offer;
    }

    public List<Offer> getOffers(OfferFilter filter) {

        String abc = "select offert from Offer offert where 1=1 ";
        if (filter.getModelId() != null) {
            abc = abc.concat("and offert.model.id = :id_model ");
        }
        if (filter.getManufacturerId() != null) {
            abc = abc.concat("and offert.model.manufacturer.id = :id_menufact ");
        }

        if (filter.getFuelTypeId() != null) {
            abc = abc.concat("and offert.fuelType.id = :fuel ");
        }
        if (filter.getYearFrom() != null) {
            abc = abc.concat("and offert.year >= :year_form ");
        }
        if (filter.getYearTo() != null) {
            abc = abc.concat("and offert.year <= :year_to ");
        }

        TypedQuery<Offer> query = em.createQuery(abc, Offer.class);
        if (filter.getModelId() != null) {
            query.setParameter("id_model", filter.getModelId());
        }
        if (filter.getManufacturerId() != null) {
            query.setParameter("id_menufact", filter.getManufacturerId());
        }
        if (filter.getFuelTypeId() != null) {
            query.setParameter("fuel", filter.getFuelTypeId());
        }
        if (filter.getYearFrom() != null) {
            query.setParameter("year_form", filter.getYearFrom());
        }
        if (filter.getYearTo() != null) {
            query.setParameter("year_to", filter.getYearTo());
        }
        List<Offer> result = query.getResultList();

        return result;
    }

    public Offer deleteOffer(Integer id) {
        Offer offer = em.find(Offer.class, id);
        em.remove(offer);

        return offer;
    }

    public Offer saveOffer(Offer offer) {
        return em.merge(offer);
    }
}
