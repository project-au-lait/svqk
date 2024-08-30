import { writable } from 'svelte/store';

class PageModel {
  constructor(public title = '') {}
}

function create() {
  const { subscribe, update } = writable(new PageModel());

  const setTitle = (title: string) => {
    update((page) => {
      page.title = title;
      return page;
    });
  };

  return {
    subscribe,
    setTitle
  };
}

export const pageStore = create();
