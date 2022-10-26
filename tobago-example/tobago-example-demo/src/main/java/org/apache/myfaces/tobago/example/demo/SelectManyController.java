package org.apache.myfaces.tobago.example.demo;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@SessionScoped
@Named
public class SelectManyController implements Serializable {

  @Inject
  private AstroData astroData;

  private List<SolarObject> planets;
  private SolarObject[] selectedPlanets = new SolarObject[0];

  @PostConstruct
  public void init() {
    planets = astroData.getSatellites("Sun");
  }

  public List<SolarObject> getPlanets() {
    return planets;
  }

  public SolarObject[] getSelectedPlanets() {
    return selectedPlanets;
  }

  public void setSelectedPlanets(final SolarObject[] selectedPlanets) {
    this.selectedPlanets = selectedPlanets;
  }
}
