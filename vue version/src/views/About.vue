<script setup lang="ts">
import ArianThread from '@/components/shared/ArianThread.vue';

</script>

<template>
  <ArianThread>
    <template #baseText>
      Search
    </template>
    <template #nextText>
      Quick Search
    </template>
  </ArianThread>
  <div class="foowrap">
    <section>
      <div class="container jump-top">
        <p>
          The Agronomic Linked Data project is supported by the <a href="https://www.southgreen.fr/" target="_blank">
            SouthGreen Bioinformatics Platform</a> in collaboration with the Institut de Recherche pour le
          Développement (IRD) and the Centre de coopération International en
          Recherche Agronomique pour le Développement (CIRAD).

        </p>
        <h4>Team:</h4>

        <div>
          <p><b>Pierre Larmande,</b><br>
            <i>Researcher, IRD</i><br>
            <i>Coordinator, Data science and knowledge management</i><br>
            <em>Pierre[dot]larmande[at]ird[dot]fr</em>
          </p>
        </div>
        <div>
          <p><b>Manuel Ruiz</b><br>
            <i>Researcher, CIRAD</i><br>
            <em>manuel[dot]ruiz[at]cirad[dot]fr</em><br>
          </p>
        </div>
        <div>
          <p><b>Gildas Tagny,</b><br>
            <i>Engineer, INRA</i><br>
            <em>tagnyngompe[at]gmail[dot]com</em>
          </p>
        </div>
        <div>
          <p><b>Yann POMIE,</b><br>
            <i>Engineer (Apprentice), IRD</i><br>
            <em>yann[dot]pomie[at]ird[dot]fr</em>
          </p>
        </div>
        <h4>Former members:</h4>
        <div>
          <p><b>Patrick Valduriez</b><br>
            <i>Senior Researcher, INRIA</i><br>
            <i>Head, Data and knowledge management workpackage, IBC</i><br>
            <em>Patrick[dot]Valduriez[at]inria[dot]fr</em><br>
          </p>
        </div>
        <div>
          <p><b>Nordine El Hassouni,</b><br>
            <i>Engineer, INRA</i><br>

          </p>
        </div>
        <div>
          <p><b>Imene Chentli,</b><br>
            <i>Project intern, IBC and Open Data Group at LIRMM</i><br>
          </p>
        </div>
        <div>
          <p><b>Idjellidaine Jean-Christophe,</b><br>
            <i>Project intern, IBC</i><br>
            <i>UX developer</i><br>

          </p>
        </div>
        <div>
          <p><b>Aravind Venkatesan,</b><br>
            <i>Post-doctoral researcher, IBC</i><br>
          </p>
        </div>
        <h4>Collaborators:</h4>
        <div>
          <p><b>Clément Jonquet </b><br>
            <i>Assistant professor, University of Montpellier</i><br>
            <i>Pi of D2KAB project</i><br>

          </p>
        </div>
        <div>
          <p><b>Konstantin Todorov</b><br>
            <i>Assistant professor, University of Montpellier</i><br>
            <i>Pi of Doremus project</i><br>

          </p>
        </div>
        <div>
          <p><b>Cyril Pommier </b><br>
            <i>Engineer, INRA</i><br>
            <i>Deputy Head of GnPIS</i><br>

          </p>
        </div>
        <div>
          <h4>Photo Credits:</h4>
          <p><b>Stephane Jouannic </b><br>
            <i>Researcher, IRD</i><br>
            <br>
          </p>
        </div>

        <div>
          <h4>Acknowledgements:</h4>
          <p>
            The project is currently supported by CGIAR Rice CRP and The French Institute of Bioinformatics (IFB - <a
              href="https://www.france-bioinformatique.fr" target="_blank"> https://www.france-bioinformatique.fr</a>)

            <br>
            This work have been supported by Institut de Biologie Computationnelle (IBC -
            <a href="http://www.ibc-montpellier.fr" target="_blank">http://www.ibc-montpellier.fr</a>),
            and the Agropolis Foundation (<a href="http://www.agropolis-fondation.fr"
              target="_blank">http://www.agropolis-fondation.fr</a>)
          </p>
        </div>
      </div>
    </section>
  </div>
  <div style="height:50px;width:100%;"></div>
</template>

<style scoped>
.foowrap {
  position: relative;
  min-height: 100%;
}
</style>
