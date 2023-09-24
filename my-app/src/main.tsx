
import ReactDOM from 'react-dom/client'
import App from './App.tsx'
import './index.css'
import {BrowserRouter} from 'react-router-dom';
import 'flowbite';
import {Provider} from "react-redux";
import {store} from "./store";
import http_common from "./http_common.ts";
import jwtDecode from "jwt-decode";
import {AuthUserActionType, IUser} from "./entities/Auth.ts";
import {ICategoryItem} from "./components/admin/category/list/types.ts";


if(localStorage.token) {
    const {token} = localStorage;
    http_common.defaults.headers.common["Authorization"]=`Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiZW1haWwiOiJhZG1pbkBnbWFpbC5jb20iLCJyb2xlcyI6WyJhZG1pbiJdLCJpc3MiOiJ2b3ZhIiwiaWF0IjoxNjk1NTY2MDAzLCJleHAiOjE2OTYxNzA4MDN9.VQIkd8EvH5nSVw01ytj0Je9fGeVJxv3WK5-5ry01J68`;
    http_common.get<ICategoryItem[]>("api/category")
        .then(resp=> {
            console.log("Server info", resp.data);
            //setList(resp.data);
        });
    const user = jwtDecode(token) as IUser;
    store.dispatch({
        type: AuthUserActionType.LOGIN_USER,
        payload: {
            sub: user.sub,
            email: user.email,
            roles: user.roles
        }
    });
}

ReactDOM.createRoot(document.getElementById('root')!).render(
    <Provider store={store}>
        <BrowserRouter>
            <App/>
        </BrowserRouter>
    </Provider>,
)
