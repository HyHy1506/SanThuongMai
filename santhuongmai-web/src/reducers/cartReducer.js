import { ADD_TO_CART, REMOVE_FROM_CART, UPDATE_CART_QUANTITY } from '../actions/cartActions';

const initialState = {
  items: JSON.parse(localStorage.getItem('cart')) || [],
};

const cartReducer = (state = initialState, action) => {
  switch (action.type) {
    case ADD_TO_CART: {
      const product = action.payload;
      const existingItem = state.items.find((item) => item.productId === product.productId);
      let newItems;
      if (existingItem) {
        newItems = state.items.map((item) =>
          item.productId === product.productId
            ? { ...item, quantity: item.quantity + 1 }
            : item
        );
      } else {
        newItems = [...state.items, { ...product, quantity: 1 }];
      }
      localStorage.setItem('cart', JSON.stringify(newItems));
      return { ...state, items: newItems };
    }
    case REMOVE_FROM_CART: {
      const newItems = state.items.filter((item) => item.productId !== action.payload);
      localStorage.setItem('cart', JSON.stringify(newItems));
      return { ...state, items: newItems };
    }
    case UPDATE_CART_QUANTITY: {
      const { productId, quantity } = action.payload;
      const newItems = state.items.map((item) =>
        item.productId === productId ? { ...item, quantity } : item
      );
      localStorage.setItem('cart', JSON.stringify(newItems));
      return { ...state, items: newItems };
    }
    default:
      return state;
  }
};

export default cartReducer;