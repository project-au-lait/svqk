import { writable } from 'svelte/store';

class MessageModel {
  constructor(
    public text = '',
    public display = false
  ) {}
}

class MessageStore {
  private writable = writable(new MessageModel());
  private update = this.writable.update;
  subscribe = this.writable.subscribe;

  show(messageText: string) {
    this.update((message) => {
      message.text = messageText;
      message.display = true;
      return message;
    });

    setTimeout(() => {
      this.update((message) => {
        message.text = '';
        message.display = false;
        return message;
      });
    }, 3000);
  }

  hide() {
    this.update((message) => {
      message.display = false;
      return message;
    });
  }
}

export const messageStore = new MessageStore();
