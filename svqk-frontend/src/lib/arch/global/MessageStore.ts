import { writable } from 'svelte/store';

class MessageModel {
  constructor(
    public text = '',
    public display = false
  ) {}
}

function create() {
  const { subscribe, update } = writable(new MessageModel());

  const show = (messageText: string) => {
    update((message) => {
      message.text = messageText;
      message.display = true;
      return message;
    });

    setTimeout(() => {
      update((message) => {
        message.text = '';
        message.display = false;
        return message;
      });
    }, 3000);
  };

  const hide = () => {
    update((message) => {
      message.display = false;
      return message;
    });
  };

  return {
    subscribe,
    show,
    hide
  };
}

export const messageStore = create();
