export const useAuth = () => {
    const token = localStorage.getItem("jwt");

    const isLoggedIn = !!token;

    return {
        isLoggedIn,
        token,
    };
};