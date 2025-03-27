import LoginForm from "@/components/ui/loginform";

import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
} from "@/components/ui/card"

function Login() {
    function handleLogin(values: { username: string; password: string}) {
        console.log(values)
    }

    return(<div className="h-screen flex justify-center items-center align-middle">
        <Card className="w-[350]px">
            <CardHeader>
                <CardTitle>Login</CardTitle>
            </CardHeader>
            <CardContent>
                <LoginForm onSubmit={handleLogin} />
            </CardContent>
        </Card>
        </div>
    )
}

export default Login;