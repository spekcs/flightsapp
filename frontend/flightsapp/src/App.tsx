import { Button } from "@/components/ui/button"
import { useState } from "react"

function App() {
  const [count, setCount] = useState(0);

  function handleClick() {
    setCount(count + 1);
  }

  return (
    <div className="flex flex-col items-center justify-center min-h-svh">
      <p>Clicked {count} times</p>
      <Button variant="outline" onClick={handleClick}>Click me</Button>
    </div>
  )
}

export default App


