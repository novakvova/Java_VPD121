import {AuthUserActions, AuthUserActionType, IAuthUser} from "../../entities/Auth.ts";

const initState: IAuthUser = {
    isAuth: false,
    user: undefined,
};

export const AuthReducer = (state = initState, action: AuthUserActions): IAuthUser => {
    switch (action.type) {
        case AuthUserActionType.LOGIN_USER: {
            return {
                isAuth: true,
                user: action.payload,
            };
        }
        case AuthUserActionType.LOGOUT_USER: {
            return {
                user: undefined,
                isAuth: false,
            };
        }
    }
    return state;
};
