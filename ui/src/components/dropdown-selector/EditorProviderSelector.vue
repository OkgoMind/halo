<script lang="ts" setup>
import { useEditorExtensionPoints } from "@/composables/use-editor-extension-points";
import {
  IconExchange,
  VAvatar,
  VDropdown,
  VDropdownItem,
} from "@halo-dev/components";
import type { EditorProvider } from "@halo-dev/console-shared";

withDefaults(
  defineProps<{
    provider?: EditorProvider;
    allowForcedSelect: boolean;
  }>(),
  {
    provider: undefined,
    allowForcedSelect: false,
  }
);

const emit = defineEmits<{
  (event: "select", provider: EditorProvider): void;
}>();

const { editorProviders, fetchEditorProviders } = useEditorExtensionPoints();

fetchEditorProviders();
</script>

<template>
  <VDropdown>
    <div
      class="group flex w-full cursor-pointer items-center gap-2 rounded p-1 hover:bg-gray-100"
    >
      <VAvatar v-if="provider?.logo" :src="provider.logo" size="xs"></VAvatar>
      <div
        class="select-none whitespace-nowrap text-sm text-gray-600 group-hover:text-gray-900"
      >
        {{ provider?.displayName }}
      </div>
      <IconExchange class="h-4 w-4 text-gray-600 group-hover:text-gray-900" />
    </div>
    <template #popper>
      <VDropdownItem
        v-for="(editorProvider, index) in editorProviders"
        :key="index"
        v-tooltip="{
          disabled:
            allowForcedSelect ||
            provider?.rawType.toLowerCase() ===
              editorProvider.rawType.toLowerCase(),
          content: $t(
            'core.components.editor_provider_selector.tooltips.disallow'
          ),
        }"
        :selected="provider?.name === editorProvider.name"
        :disabled="
          !allowForcedSelect &&
          provider?.rawType.toLowerCase() !==
            editorProvider.rawType.toLowerCase()
        "
        @click="emit('select', editorProvider)"
      >
        <template v-if="editorProvider.logo" #prefix-icon>
          <VAvatar :src="editorProvider.logo" size="xs"></VAvatar>
        </template>
        {{ editorProvider.displayName }}
      </VDropdownItem>
    </template>
  </VDropdown>
</template>
