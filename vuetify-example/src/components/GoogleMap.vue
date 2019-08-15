<template>
  <v-container>
    <v-layout
      text-center
      wrap
    >
      <v-flex xs12>
        <h1>2地点のルート</h1>
        <v-form
          ref="directionForm"
          v-model="direction.valid"
          >
          <v-row text-centerr>
            <v-col
              cols="12"
              md="4"
              >
              <v-text-field
                v-model="direction.origin"
                label="起点"
                prepend-icon="place"
                :rules="pointRules"
                required
                ></v-text-field>
            </v-col>
          </v-row>
          <v-row>
            <v-col
              cols="12"
              md="4"
              >
              <v-text-field
                v-model="direction.destination"
                label="目的地"
                prepend-icon="place"
                :rules="pointRules"
                required
                ></v-text-field>
            </v-col>
          </v-row>

          <v-row>
            <v-col cols="12" md="4">
              <v-checkbox
                v-model="direction.openInNewTab"
                label="新しいタブで開く"
                ></v-checkbox>
            </v-col>
          </v-row>

          <v-row>
            <v-col cols="12" md="4">
              <a v-if="direction.valid"
                 :target="directionTarget"
                 required
                 :href="directionUrl"
                 ><v-icon :color="direction.validColor">mdi-google-maps</v-icon>Google Map Direction Search</a>
              <p v-else-if="!direction.valid"><v-icon>mdi-google-maps</v-icon>Google Map Direction Search</p>
            </v-col>
          </v-row>

        </v-form>
      </v-flex>

    </v-layout>
  </v-container>
</template>

<script>
export default {
  data: () => ({
    direction: {
      valid: false,
      validColor: 'blue',
      openInNewTab: true,
      origin: '',
      destination: '',
    },
    pointRules: [
      v => !!v || 'Required',
    ],
  }),
  computed: {
    directionTarget() {
      return this.direction.openInNewTab ? '_blank' : '_self';
    },
    directionUrl() {
      return `http://www.google.com/maps/dir/?api=1&origin=${encodeURIComponent(this.direction.origin)}&destination=${encodeURIComponent(this.direction.destination)}`;
    },
  },
  methods: {
  },
};
</script>
